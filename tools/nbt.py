"""Minimal NBT reader/writer for structure templates (gzip)."""
import gzip, io, struct

TAG_END=0; TAG_BYTE=1; TAG_SHORT=2; TAG_INT=3; TAG_LONG=4; TAG_FLOAT=5; TAG_DOUBLE=6
TAG_BYTE_ARRAY=7; TAG_STRING=8; TAG_LIST=9; TAG_COMPOUND=10; TAG_INT_ARRAY=11; TAG_LONG_ARRAY=12

def _read_string(f):
    n=struct.unpack('>H', f.read(2))[0]
    return f.read(n).decode('utf-8')

def _read_payload(f, t):
    if t==TAG_BYTE: return struct.unpack('>b', f.read(1))[0]
    if t==TAG_SHORT: return struct.unpack('>h', f.read(2))[0]
    if t==TAG_INT: return struct.unpack('>i', f.read(4))[0]
    if t==TAG_LONG: return struct.unpack('>q', f.read(8))[0]
    if t==TAG_FLOAT: return struct.unpack('>f', f.read(4))[0]
    if t==TAG_DOUBLE: return struct.unpack('>d', f.read(8))[0]
    if t==TAG_BYTE_ARRAY:
        n=struct.unpack('>i',f.read(4))[0]; return list(f.read(n))
    if t==TAG_STRING: return _read_string(f)
    if t==TAG_LIST:
        et=struct.unpack('>B',f.read(1))[0]; n=struct.unpack('>i',f.read(4))[0]
        return [_read_payload(f,et) for _ in range(n)]
    if t==TAG_COMPOUND:
        d={}
        while True:
            tt=struct.unpack('>B',f.read(1))[0]
            if tt==TAG_END: break
            name=_read_string(f)
            d[name]=_read_payload(f,tt)
        return d
    if t==TAG_INT_ARRAY:
        n=struct.unpack('>i',f.read(4))[0]; return list(struct.unpack('>%di'%n,f.read(4*n)))
    if t==TAG_LONG_ARRAY:
        n=struct.unpack('>i',f.read(4))[0]; return list(struct.unpack('>%dq'%n,f.read(8*n)))
    raise ValueError('tag %d'%t)

def read(data):
    f=gzip.GzipFile(fileobj=io.BytesIO(data)) if not hasattr(data,'read') else data
    t=struct.unpack('>B',f.read(1))[0]
    if t!=TAG_COMPOUND: raise ValueError('root not compound')
    _read_string(f)
    return _read_payload(f,TAG_COMPOUND)

def _write_string(f,s):
    b=s.encode('utf-8'); f.write(struct.pack('>H',len(b))); f.write(b)

def _tag_type(v):
    if isinstance(v,bool) or isinstance(v,int) and -128<=v<=127 and False: return TAG_BYTE
    if isinstance(v,dict): return TAG_COMPOUND
    if isinstance(v,list):
        if not v: return TAG_LIST
        return TAG_LIST
    if isinstance(v,str): return TAG_STRING
    if isinstance(v,float): return TAG_DOUBLE
    if isinstance(v,int):
        return TAG_LONG if abs(v)>2**31-1 else TAG_INT
    raise TypeError(type(v))

def _write_payload(f,t,v):
    if t==TAG_BYTE: f.write(struct.pack('>b',v))
    elif t==TAG_SHORT: f.write(struct.pack('>h',v))
    elif t==TAG_INT: f.write(struct.pack('>i',v))
    elif t==TAG_LONG: f.write(struct.pack('>q',v))
    elif t==TAG_FLOAT: f.write(struct.pack('>f',v))
    elif t==TAG_DOUBLE: f.write(struct.pack('>d',v))
    elif t==TAG_STRING: _write_string(f,v)
    elif t==TAG_COMPOUND:
        for k,val in v.items():
            ft=_tag_type(val); f.write(struct.pack('>B',ft)); _write_string(f,k); _write_payload(f,ft,val)
        f.write(b'\x00')
    elif t==TAG_LIST:
        if not v:
            f.write(struct.pack('>B',TAG_END)); f.write(struct.pack('>i',0)); return
        ft=_tag_type(v[0]); f.write(struct.pack('>B',ft)); f.write(struct.pack('>i',len(v)))
        for x in v: _write_payload(f,ft,x)
    elif t==TAG_INT_ARRAY:
        f.write(struct.pack('>i',len(v))); f.write(struct.pack('>%di'%len(v),*v))
    elif t==TAG_LONG_ARRAY:
        f.write(struct.pack('>i',len(v))); f.write(struct.pack('>%dq'%len(v),*v))
    else: raise ValueError(t)

def write(nbt):
    raw=io.BytesIO()
    raw.write(struct.pack('>B',TAG_COMPOUND)); _write_string(raw,''); _write_payload(raw,TAG_COMPOUND,nbt)
    return gzip.compress(raw.getvalue())

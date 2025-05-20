from kafi.kafi import *
c = Cluster("local")
p = c.producer("minimal-resume")
key_str = f"{int(time.time()*1000)}"
print(p.produce("new:{}", key=key_str))
print(p.produce("step1:=42", key=key_str))
print(p.produce("step2:=null", key=key_str))
p.close()

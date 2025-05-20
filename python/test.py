from kafi.kafi import *
c = Cluster("local")
t = "minimal-resume"
c.retouch(t)
p = c.producer(t)
p.produce("new:{}", key="2")
p.produce("step1:=42", key="2")
p.produce("step2:=null", key="2")
p.close()

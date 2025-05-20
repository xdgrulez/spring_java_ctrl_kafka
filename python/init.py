from kafi.kafi import *
c = Cluster("local")
print(c.retouch("minimal-resume"))
print(c.retouch("minimal-result"))
print(c.retouch("minimal-app-store-changelog"))

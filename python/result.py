from kafi.kafi import *
c = Cluster("local")
print(c.cat("minimal-result"))

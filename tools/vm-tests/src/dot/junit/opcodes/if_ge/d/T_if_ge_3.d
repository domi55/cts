.source T_if_ge_3.java
.class public dot.junit.opcodes.if_ge.d.T_if_ge_3
.super java/lang/Object


.method public <init>()V
.limit regs 1

       invoke-direct {v0}, java/lang/Object/<init>()V
       return-void
.end method

.method public run(FI)I
.limit regs 8

       if-ge v6, v7, Label11
       const/16 v6, 1234
       return v6

Label11:
       const/4 v6, 1
       return v6
.end method

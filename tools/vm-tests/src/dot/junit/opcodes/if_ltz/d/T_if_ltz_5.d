.source T_if_ltz_5.java
.class public dot.junit.opcodes.if_ltz.d.T_if_ltz_5
.super java/lang/Object


.method public <init>()V
.limit regs 1

       invoke-direct {v0}, java/lang/Object/<init>()V
       return-void
.end method

.method public run(J)I
.limit regs 6

       if-ltz v4, Label9
       const/16 v4, 1234
       return v4

Label9:
       const/4 v4, 1
       return v4
.end method

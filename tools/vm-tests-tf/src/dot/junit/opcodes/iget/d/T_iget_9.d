; Copyright (C) 2008 The Android Open Source Project
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;      http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

.source T_iget_9.java
.class public dot.junit.opcodes.iget.d.T_iget_9
.super java/lang/Object

.field public  val F

.method  <clinit>()V
.limit regs 2
       return-void
.end method

.method public <init>()V
.limit regs 2

       invoke-direct {v1}, java/lang/Object/<init>()V
       return-void
.end method

.method public run()F
.limit regs 4

       const v0, 0
       iget v1, v0, dot.junit.opcodes.iget.d.T_iget_9.val F
       return v1
.end method



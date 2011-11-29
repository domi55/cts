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

.source ITestImplAbstract.java
.class public dxc/junit/opcodes/invokeinterface/jm/ITestImplAbstract
.super java/lang/Object
.implements dxc/junit/opcodes/invokeinterface/jm/ITest

.method  <init>()V
    aload_0
    invokespecial java/lang/Object/<init>()V
    return
.end method


.method public abstract doit()V
.end method



.method public static doit(I)V
    .limit locals 2
    return
.end method



.method public native doitNative()V
.end method

.method protected test(I)I
    .limit locals 2
    .limit stack 1
    iconst_0
    ireturn
.end method

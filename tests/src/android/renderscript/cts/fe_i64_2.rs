#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)

void root(const long2 *ain, ulong2 *aout) {
    aout[0].x = ain[0].x + 1;
    aout[0].y = ain[0].y + 1;
    return;
}

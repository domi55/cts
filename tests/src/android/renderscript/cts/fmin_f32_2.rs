#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)

typedef struct fmin_f32_2_in {
    float2 first;
    float2 second;
} input;

void root(const input* in, float2* out){
    *out = fmin(in->first, in->second);
}

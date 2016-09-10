# Copyright 2014 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import its.image
import its.caps
import its.device
import its.objects
import os.path
import pylab
import matplotlib
import matplotlib.pyplot
import numpy as np

#AE must converge within this number of auto requests for EV
THRESH_CONVERGE_FOR_EV = 8
YUV_FULL_SCALE = 255.0
YUV_SATURATION_MIN = 253.0


def main():
    """Tests that EV compensation is applied.
    """
    LOCKED = 3

    NAME = os.path.basename(__file__).split(".")[0]

    with its.device.ItsSession() as cam:
        props = cam.get_camera_properties()
        its.caps.skip_unless(its.caps.ev_compensation(props) and
                             its.caps.ae_lock(props))

        ev_per_step = its.objects.rational_to_float(
                props['android.control.aeCompensationStep'])
        steps_per_ev = int(1.0 / ev_per_step)
        evs = range(-2 * steps_per_ev, 2 * steps_per_ev + 1, steps_per_ev)
        lumas = []
        reds = []
        greens = []
        blues = []

        # Converge 3A, and lock AE once converged. skip AF trigger as
        # dark/bright scene could make AF convergence fail and this test
        # doesn't care the image sharpness.
        cam.do_3a(ev_comp=0, lock_ae=True, do_af=False)

        for ev in evs:

            # Capture a single shot with the same EV comp and locked AE.
            req = its.objects.auto_capture_request()
            req['android.control.aeExposureCompensation'] = ev
            req["android.control.aeLock"] = True
            caps = cam.do_capture([req]*THRESH_CONVERGE_FOR_EV)
            for cap in caps:
                if (cap['metadata']['android.control.aeState'] == LOCKED):
                    y = its.image.convert_capture_to_planes(cap)[0]
                    tile = its.image.get_image_patch(y, 0.45,0.45,0.1,0.1)
                    lumas.append(its.image.compute_image_means(tile)[0])
                    rgb = its.image.convert_capture_to_rgb_image(cap)
                    rgb_tile = its.image.get_image_patch(rgb,
                                                       0.45, 0.45, 0.1, 0.1)
                    rgb_means = its.image.compute_image_means(rgb_tile)
                    reds.append(rgb_means[0])
                    greens.append(rgb_means[1])
                    blues.append(rgb_means[2])
                    break
            assert(cap['metadata']['android.control.aeState'] == LOCKED)

        pylab.plot(evs, lumas, '-ro')
        pylab.xlabel('EV Compensation')
        pylab.ylabel('Mean Luma (Normalized)')
        matplotlib.pyplot.savefig("%s_plot_means.png" % (NAME))

        # Trim extra saturated images
        while lumas and lumas[-1] >= YUV_SATURATION_MIN/YUV_FULL_SCALE:
            if reds[-1] == greens[-1] == blues[-1]:
                lumas.pop(-1)
                reds.pop(-1)
                greens.pop(-1)
                blues.pop(-1)
        # Only allow positive EVs to give saturated image
        assert(len(lumas) > 2)
        luma_diffs = np.diff(lumas)
        min_luma_diffs = min(luma_diffs)
        print "Min of the luma value difference between adjacent ev comp: ", \
                min_luma_diffs
        # All luma brightness should be increasing with increasing ev comp.
        assert(min_luma_diffs > 0)

if __name__ == '__main__':
    main()

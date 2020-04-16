#include <math.h>
#include "hb_util.hh"

float HBFixedToFloat(hb_position_t v) {
  return scalbnf(v, -8); // div 256
}

hb_position_t HBFloatToFixed(float v) {
  return scalbnf(v, +8); // mul 256
}

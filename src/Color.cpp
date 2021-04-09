#include "Color.hpp"

namespace window_management {

Color::Color(short r_, short g_, short b_): 
    r{r_}, g{g_}, b{b_} {}

std::size_t Color::getPixel() {
    return b + (g << 8) + (r << 16);
}

} // namespace window_management
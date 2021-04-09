#pragma once

#include <cstddef>

namespace window_management {

struct Color {
    short r;
    short g;
    short b;

    Color(short r, short g, short b);
    
    std::size_t getPixel();
};

} // namespace window_management
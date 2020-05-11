include_directories(include)

aux_source_directory(src DIR_SRCS)

add_library(crypto ${DIR_SRCS})
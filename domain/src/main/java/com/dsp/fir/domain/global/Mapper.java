package com.dsp.fir.domain.global;

import java.util.List;

public interface Mapper<Src, Dst> {
    Dst map(Src entity);

    List<Dst> map(List<Src> entities);
}
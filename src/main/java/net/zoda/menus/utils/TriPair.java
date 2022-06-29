package net.zoda.menus.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class TriPair<A,B,C> {

    @Getter @NotNull private final A a;
    @Getter @NotNull private final B b;
    @Getter @NotNull private final C c;

}

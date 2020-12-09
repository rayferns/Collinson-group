package com.rayferns.vendingMachine.controller;

import com.rayferns.vendingMachine.display.IDisplay;

/**
 * An abstract implementation, that centralises the coin dispatching and the display reference
 * Created by BPerlakiH on 12/05/2017.
 */
abstract class AbstractController implements IInput {

    final IDisplay display;
    final IOutputDelegate delegate;

    AbstractController(IDisplay display, IOutputDelegate delegate) {
        assert (display != null);
        assert (delegate != null);
        this.delegate = delegate;
        this.display = display;
    }

}

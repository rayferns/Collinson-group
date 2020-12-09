package com.rayferns.vendingMachine;

/**
 * A delegate / call-back interface for the Transaction to trigger durring various scenarios
 * Created by BPerlakiH on 19/05/2017.
 */
public interface ITransactionDelegate {

    void onContinue();

    void onComplete();

    void onOutOfChange();
}

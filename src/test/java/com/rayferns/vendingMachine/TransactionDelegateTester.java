package com.rayferns.vendingMachine;

/**
 * A test implementation, which helps the testing of the ITransactionDelegate
 * Created by BPerlakiH on 19/05/2017.
 */
public class TransactionDelegateTester implements ITransactionDelegate {

    private TransactionState currentState = TransactionState.ONGOING;

    @Override
    public void onContinue() {
        currentState = TransactionState.ONGOING;
    }

    @Override
    public void onComplete() {
        currentState = TransactionState.COMPLETE;
    }

    @Override
    public void onOutOfChange() {
        currentState = TransactionState.OUT_OF_CHANGE;
    }


    boolean isComplete() {
        return isState(TransactionState.COMPLETE);
    }

    boolean isOngoing() {
        return isState(TransactionState.ONGOING);
    }

    boolean isOutOfChange() {
        return isState(TransactionState.OUT_OF_CHANGE);
    }


    private boolean isState(TransactionState state) {
        return currentState == state;
    }
}

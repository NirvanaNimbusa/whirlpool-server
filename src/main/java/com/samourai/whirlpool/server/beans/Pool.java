package com.samourai.whirlpool.server.beans;

import com.samourai.whirlpool.protocol.WhirlpoolProtocol;

public class Pool {
  private String poolId;
  private long denomination; // in satoshis
  private PoolFee poolFee;
  private long minerFeeMin; // in satoshis
  private long minerFeeCap; // in satoshis
  private long minerFeeMax; // in satoshis
  private long minerFeeMix; // in satoshis
  private int minMustMix;
  private int minLiquidity;
  private int anonymitySet;

  private Mix currentMix;
  private InputPool mustMixQueue;
  private InputPool liquidityQueue;

  public Pool(
      String poolId,
      long denomination,
      PoolFee poolFee,
      long minerFeeMin,
      long minerFeeCap,
      long minerFeeMax,
      long minerFeeMix,
      int minMustMix,
      int minLiquidity,
      int anonymitySet) {
    this.poolId = poolId;
    this.denomination = denomination;
    this.poolFee = poolFee;
    this.minerFeeMin = minerFeeMin;
    this.minerFeeCap = minerFeeCap;
    this.minerFeeMax = minerFeeMax;
    this.minerFeeMix = minerFeeMix;
    this.minMustMix = minMustMix;
    this.minLiquidity = minLiquidity;
    this.anonymitySet = anonymitySet;
    this.mustMixQueue = new InputPool();
    this.liquidityQueue = new InputPool();
  }

  public boolean checkInputBalance(long inputBalance, boolean liquidity) {
    long minBalance = computePremixBalanceMin(liquidity);
    long maxBalance = computePremixBalanceMax(liquidity);
    return inputBalance >= minBalance && inputBalance <= maxBalance;
  }

  public long computePremixBalanceMin(boolean liquidity) {
    return WhirlpoolProtocol.computePremixBalanceMin(
        denomination, computeMustMixBalanceMin(), liquidity);
  }

  public long computePremixBalanceCap(boolean liquidity) {
    return WhirlpoolProtocol.computePremixBalanceMax(
        denomination, computeMustMixBalanceCap(), liquidity);
  }

  public long computePremixBalanceMax(boolean liquidity) {
    return WhirlpoolProtocol.computePremixBalanceMax(
        denomination, computeMustMixBalanceMax(), liquidity);
  }

  public long computeMustMixBalanceMin() {
    return denomination + minerFeeMin;
  }

  public long computeMustMixBalanceCap() {
    return denomination + minerFeeCap;
  }

  public long computeMustMixBalanceMax() {
    return denomination + minerFeeMax;
  }

  public String getPoolId() {
    return poolId;
  }

  public long getDenomination() {
    return denomination;
  }

  public PoolFee getPoolFee() {
    return poolFee;
  }

  public long getMinerFeeMin() {
    return minerFeeMin;
  }

  public long getMinerFeeCap() {
    return minerFeeCap;
  }

  public long getMinerFeeMax() {
    return minerFeeMax;
  }

  public long getMinerFeeMix() {
    return minerFeeMix;
  }

  public int getMinMustMix() {
    return minMustMix;
  }

  public int getMinLiquidity() {
    return minLiquidity;
  }

  public int getAnonymitySet() {
    return anonymitySet;
  }

  public Mix getCurrentMix() {
    return currentMix;
  }

  public void setCurrentMix(Mix currentMix) {
    this.currentMix = currentMix;
  }

  public InputPool getMustMixQueue() {
    return mustMixQueue;
  }

  public InputPool getLiquidityQueue() {
    return liquidityQueue;
  }
}

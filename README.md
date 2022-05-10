### Collaboration Rules
This lab  is an individual work. Everyone should submit their own work. You can use any material from the course, or any other resource on Java programming language. You can discuss lecture related topics, the problem at the concept level with other students, but not the solution. You can reuse (copy-paste) code from the lecture notes, textbook, or code given in the lab, but not from others (internet sources, other students etc.)

### Honor Code - by submitting lab work I accept:
- This code is my own work. 
- I did not consult to any program written other students. 
- I did not search for the solution of the problem in the internet.
- I did not copy-paste code written by others.
- This code is my own work.

### IMPORTANT

* DON'T COPY PASTE code from anybody or any other resource. The code should be written by you. The code similarity tools will find such codes and disciplinary action will be taken.
* You should solve the problem the way we described. By not following instructions, you may lose points.
* Check Blackboard LAB 10 for possible corrections.
* Print out in the same format, points will be taken off, if you don't print in the same format.
* It is normal for you get "cannot be resolved" type of errors when you first import your project from Github because all classes are not implemented yet. You will resolve these errors as you create and implement the necessary classes.
* Pay attention to use the appropriate access modifiers for instance variables and methods.
* **Pay attention for synchronization.** 

### LATE SUBMISSION 

Each Lab has its late submission rule. Lab 10 late submission is :
* Regular Submission: Noon 12pm May 28, at the end of the lab
* Late Submission: May 28 (only if you attend the lab and tell your that you will submit late)
   * 12:05: -2 points
   * 12:10: -5 points
   * 23:59: -45 points

# Lab 10: Crypto Exchange with Shared Buffer
The focus of this lab is **concurrency** and **producer-consumer relationship**.You are asked to implement an application that emulates a scenario where there is a crypto exchange platform (such as Coinbase, Binance, Gate.io etc.), and there are two investors (Satoshi and Vitalik) who want to buy and sell a crypto coin. We will simulate a simple buying-selling platform. 

The producer is the exchange platform (represented by the class ExchangePlatform), and consumers are repsented by the Investor class (so Satoshi and Vitalik are istances of Investor class).

The producer produces the current price and makes it aavilable to the consumers. Consumers then consumes this price buy buying it. The details of producer and consumer will be explained below in the related classes.

You are asked to implement a multithreaded implementation of this problem. 

You need to use ExecutorService, and Runnable classes, and synchronized shared buffer to achieve concurrency. 

In the prelab - the shared buffer will be implemented using Object methods wait-nofityAll methods. See Fig23.26 (textbook slides, code) for the use of wait-notifyAll for implementing synchronized shared buffer.

# Given Classes
## Main Class
This class contains the main method of the program. This class contains the initialization of the ExecutorService, shared buffer and investors. Each investor and exchange platform **runs on a different thread** and **share the same buffer**.  We provide this class.

## Logger
This class simply prints a string. We provide this class. Use this class to print messages.

## Buffer Interface
Buffer interface has two methods:
```java
void addLatestPrice(float value); // adds the latest price to the buffer, if the buffer is empty
float getLatestPrice(); // gets the latest price in the buffer, if the buffer contains the price, and buffer becomes empty
float checkLatestPrice(); // jsut returns the current price in teh buffer if the buffer contains price, otherwise returns -1
```
# Classes to be implemented

## SynchBuffer Class
This class implements Buffer interface and represents the buffer shared by ExchangePlatform and Investor. The buffer holds the latest price (a single value). It defines addLatestPrice(), getLatestPrice(), and checkLatestPrice() methods to manipulate the buffer in an atomic way (hint: check **synchronized** keyword). getLatestPrice() returns the latest price, and the buffer becomes empty. If there is no price in the buffer, the calling thread waits until the buffer becomes available.  addLatestPrice() puts the price into the buffer if the buffer is available otherwise the calling thread waits until the buffer becomes available. checkLatestPrice() simply returns the price in the buffer (if the buffer is empty, it returns -1). checkLatestPrice() does not consume the value in the buffer.

IMPORTANT: In prelab, you are asked to implement this synchronized buffer using Object wait-notifyAll methods.

## ExchangePlatform Class
The main purpose of the ExchangePlatform is to provide the latest price of the coin (Hence, it acts as a producer in this scenario). The constructor takes  three parameters: 

```java
public ExchangePlatform(Buffer exchangeBuffer, float midPrice, float variance)
```

It provides the latest price of the coin with the following formula:

```java
latestPrice = midPrice - variance + 2 * variance * generator.nextFloat();
```

For example, if the midPrice is provided as 50.000 and variance is 2.000, it produces values between 48.000 and 52.000.
Exchange platform produces the latest price 10 times, sleeping randomly in between (sleeping 2 seconds max).

After it generates the latest price, it adds the latestPrice value to the buffer by using buffer's **addLatestPrice** method. 

The logic of the ExchangePlatform is as follows:
```
   for 10 times
       sleep  a random time (max 2 seconds).
       produce latest price
       print message
```

Here is the print message that we used:
```
Logger.displayState("Latest price set by the crypto exchange platform: " + latestPrice);
```


## Investor Class
This class represents an investor. An investor's life is as follows:

```
    sleep a random time (max 1 second)
    if not bought yet any coin
       just buy the coint at the current marketprice (consume price)
       print message
    if already bought coin previously
       sell the coin according to the takeProfit or stopLoss,
       as described below
       print message in case of takeProfit or stopLoss    
       If there is no currentPrice available, or takeProfit/stopLoss
       conditions are not met, keep holding coin (do nothing)
       
```

Here are the print messages:

```
Logger.displayState("[" + name + "]" + ", Bought at: " + latestPrice");

Logger.displayState(
"[" + name + "]" + " Take profit at: " + (buyingPrice + takeProfit) + ", Bought at: " + buyingPrice + ", Sold at: " + 
							latestPrice + ", Profit: " + (latestPrice - buyingPrice);


Logger.displayState(
    "[" + name + "]" + " Stop loss at " + (buyingPrice - stopLoss) + ", Bought at: " + buyingPrice + 
							       ", Sold at: " + latestPrice + 
							       ", Loss: " + (latestPrice - buyingPrice);
```

Note that, if the investor buys the coin it consumes the price in the shared buffer, howeverm checking the latest proce does not consume it.

Investor class has one constructor that takes two parameters: 
```java
Investor(Buffer exchangeBuffer, String name,  float takeProfit, float stopLoss)
```
* **exchangeBuffer**: Shared buffer that holds the latest price.
* **name**: Name of the investor
* **takeProfit** = This is the minimum amount of profit that the investor wants to make. For example, if the buying price is 5000 dollars and the takeProfit is set to 200, then the investor will close the position once the price hits above 5200 dollars to take a profit. Let's say the new price is 5300 dollars. It sells the coin at 5300 instead of 5200 and takes 300 dollars profit. 
* **stopLoss** = This is the loss threshold that the investor defines. For example, if the buying price is 5000 and stopLoss is 200, then the investor will close the position once the prices goes below 4800 to cut the loss. Let's say the new price is 4600 dollars. The investor sells the coin at 4600 instead of 4800 and loses 400 dollars (you might think that this does not make sense and ask why don't we sell from 4800 as soon as it hits the 4800 but in volatile markets, it is often not possible to sell at exactly the buying price - stop loss price because of the volatility). 

Note that selling is just printing the takeProfit/stopLost message, and changing to state to 'not bought". It does not involve consume operation.

Be aware that you might need to define other instance variables to remember if previously bought coin, and its price etc. If the investor has already bought the coin, they cannot buy again before selling the coin in their hand. The selling can be in two ways as you can unerstand from the explanation above (either take a profit or stop loss).

Note that the output of the program is different for each run. What er will check is for consistency. For example, producer can not put two price vlaues consecutively. produce-consume-produce-consume order should be consisten.

Note that the program maynot terminate. For example, investors may not find suitable to sell the coin thay have and they decide waiting. If no investors consumes the latest price, then the producer will not produce new price etc. 

In short, we will check the output for consistency. Here is a sample output:


## Sample output
```
Latest price set by the crypto exchange platform: 51217.414
[Satoshi], Bought at: 51217.414
Latest price set by the crypto exchange platform: 51352.43
[Vitalik], Bought at: 51352.43
Latest price set by the crypto exchange platform: 48310.527
[Vitalik] Stop loss at 51152.43, Bought at: 51352.43, Sold at: 48310.527, Loss: -3041.9023
[Satoshi] Stop loss at 50917.414, Bought at: 51217.414, Sold at: 48310.527, Loss: -2906.8867
[Vitalik], Bought at: 48310.527
Latest price set by the crypto exchange platform: 51093.977
[Satoshi], Bought at: 51093.977
Latest price set by the crypto exchange platform: 47934.582
[Vitalik] Stop loss at 48110.527, Bought at: 48310.527, Sold at: 47934.582, Loss: -375.9453
[Satoshi] Stop loss at 50793.977, Bought at: 51093.977, Sold at: 47934.582, Loss: -3159.3945
[Satoshi], Bought at: 47934.582
Latest price set by the crypto exchange platform: 50486.63
[Vitalik], Bought at: 50486.63
Latest price set by the crypto exchange platform: 50853.44
[Satoshi] Take profit at: 49934.582, Bought at: 47934.582, Sold at: 50853.44, Profit: 2918.8594
[Satoshi], Bought at: 50853.44
Latest price set by the crypto exchange platform: 47966.2
[Vitalik] Stop loss at 50286.63, Bought at: 50486.63, Sold at: 47966.2, Loss: -2520.4297
[Satoshi] Stop loss at 50553.44, Bought at: 50853.44, Sold at: 47966.2, Loss: -2887.2422
[Satoshi], Bought at: 47966.2
Latest price set by the crypto exchange platform: 48532.13
[Vitalik], Bought at: 48532.13
Latest price set by the crypto exchange platform: 51259.51
[Vitalik] Take profit at: 50232.13, Bought at: 48532.13, Sold at: 51259.51, Profit: 2727.3828
[Satoshi] Take profit at: 49966.2, Bought at: 47966.2, Sold at: 51259.51, Profit: 3293.3125
[Satoshi], Bought at: 51259.51
....
```


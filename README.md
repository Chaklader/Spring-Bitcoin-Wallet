
Bitcoin Wallet App
------------------

What is it?
-----------

Bitcoin wallet application to generate multiple wallets (e.g addresses) with functionality such as
balanace checking, transaction history and send money from the wallet to external wallet.


![alt Wallet App UI](https://github.com/Chaklader/Spring-Bitcoin-Wallet/blob/master/src/main/resources/App_UI.png)
                    image:Wallet App UI

Improvement
-----------

A. Should implement a delete button to delete a wallet
(e.g address) and the associated information's from
the database.

B. Needs some upgrade of the performance such as
synchronization and data insertion to the database.


The Latest Version
------------------

Documentation
-------------

Installation
------------

Licensing
---------

Cryptographic Software Notice
-----------------------------


Contacts
--------


For the first time, To simply build the entire project - just run the standard Maven build:
-------------------------------------------------------------------------------------------

mvn clean install


Later to skips the tests, run with the command:
-----------------------------------------------

mvn clean install -Dmaven.test.skip=true



This repo uses various ML and AI tools to predict the outcomes of races between fictional creatures with various stats called `chao` in the Nintendo Gamecube game Sonic Adventure 2 Balle.
It also implements several search algorithms to asist with swapping these fictional creatures between memory cards efficiantly.

Requires the use of a local MySQL DB to store data

to run the main java code:

`java -cp path/to/SA2B/bin:/Users/alecmacrae/personalWorkspace/SA2B/mysql-connector-java-5.1.31/mysql-connector-java-5.1.31-bin.jar:/Users/alecmacrae/personalWorkspace/SA2B/guava-17.0.jar chao.ChaoManager commands /Users/alecmacrae/Desktop/chao_order.txt`

you can also set an alias if you would like

`alias chao='java -cp path/to/SA2B/bin:/Users/alecmacrae/personalWorkspace/SA2B/mysql-connector-java-5.1.31/mysql-connector-java-5.1.31-bin.jar:/Users/alecmacrae/personalWorkspace/SA2B/guava-17.0.jar chao.ChaoManager'`

then just run

`chao commands`

Python/R/ipython notebook code should be more self explanitory to run

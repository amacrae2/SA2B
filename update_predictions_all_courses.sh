#!/bin/bash

chao() {
	java -cp /Users/alecmacrae/personalWorkspace/SA2B/bin:/Users/alecmacrae/personalWorkspace/SA2B/mysql-connector-java-5.1.31/mysql-connector-java-5.1.31-bin.jar:/Users/alecmacrae/personalWorkspace/SA2B/guava-17.0.jar chao.ChaoManager "$@"
}

declare -a arr=("crab_pool" "stump_valley" "mushroom_forest" "block_canyon" "aquamarine" "topaz" "topaz" "garnet" "onix" "diamond" "overall" "karate")

for i in "${arr[@]}"
do
	chao predict_result "$i"
	chao predict_result_unfiltered "$i"
done


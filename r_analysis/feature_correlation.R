# basic correlation analysis of the features

data = read.table("../chao_stats.txt", header = TRUE, sep = "\t")

cor(data[,4:20])

summary(data)

# install.packages("polycor")
library(polycor)
hetcor(data[,4:20]) # if this does not work try changing 20 to 15

# install.packages("corrgram")
library(corrgram)
# The corrgram function produces a graphical display of a correlation matrix, called a correlogram. 
# The cells of the matrix can be shaded or colored to show the correlation value.
corrgram(data[,4:20])

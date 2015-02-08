# shows the theta values over 1000 iterations of gradient descent for the various models
# Also shows error over the iterations if param = "error"

param = "swim" # options include swim, fly, run, power, error (and for unfiltered only: hat, int, trips, and luck)

getBaseIndex <- function(s) {
  if (s == "swim") {
    return(1)
  } else if (s == "fly") {
    return(2)
  } else if (s == "run") {
    return(3)
  } else if (s == "power") {
    return(4)
  } else if (s == "error") {
    return(5)
  } else {
    return(NULL)
  }
}

getIndex <- function(s, filter) {
  x = getBaseIndex(s)
  if (x != NULL) {
    if (s = "error") {
      return(10)
    } else {
      return(x)
    }
  } else {
    if (s == "stamina") {
      return(5)
    } else if (s == "hat") {
      return(6)
    } else if (s == "int") {
      return(7)
    } else if (s == "trips") {
      return(8)
    } else if (s == "luck") {
      return(9)
    }
  }
}

# filtered Results
x = getIndex(param,TRUE)

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_filtered_stochastic.csv", header = FALSE, sep = ",")
plot(data[,x][1:1000],type="o")

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_filtered_batch.csv", header = FALSE, sep = ",")
plot(data[,x][1:1000],type="o")

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_filtered_final_stochastic.csv", header = FALSE, sep = ",")
plot(data[,x][1:1000],type="o")

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_filtered_final_batch.csv", header = FALSE, sep = ",")
plot(data[,x][1:1000],type="o")

# Unfiltered results
x = getIndex(param,FALSE)

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_unfiltered_stochastic.csv", header = FALSE, sep = ",")
plot(data$V10[1:1000],type="o")

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_unfiltered_batch.csv", header = FALSE, sep = ",")
plot(data$V10[1:1000],type="o")

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_unfiltered_final_stochastic.csv", header = FALSE, sep = ",")
plot(data$V1[1:1000],type="o")

data = read.table("/Users/alecmacrae/personalWorkspace/SA2B/chao_thetas_unfiltered_final_batch.csv", header = FALSE, sep = ",")
plot(data$V1[1:1000],type="o")



# install.packages("Hmisc")
library(Hmisc)

rm(list=ls())

#all_args = commandArgs(trailingOnly = TRUE);

collapsedFiles = list.files(pattern="collapsed.*.in.txt")

#par(mfrow = c(1,2)) #2 plots in 1
i = 1
for (file in collapsedFiles) {
  data = read.table(file)[,1]
  data = data/86400 #s in d
  if (i == 1) {
    result = matrix(data, ncol=1)
  } else {
    result = cbind(result,data)
  }
  i = i+1
}


collapsedFiles = gsub("collapsed.", "", collapsedFiles)
collapsedFiles = gsub(".in.txt", "", collapsedFiles)
collapsedFiles = gsub("n", "n=", collapsedFiles)
collapsedFiles = gsub(".s", ", s=", collapsedFiles)
colnames(result) = collapsedFiles

#significance level
alpha = 0.05

pdf("summary.pdf", title="BeeSimulation: Scenario Comparison")
boxplot(result, main="Scenario comparison: Time passed until all bees are dead", xlab="Scenario", ylab="Simulation time (d)")
for (j in 1:ncol(result)) {
  m = mean(result[,j])
  error = qt(1-alpha/2,df=length(result[,j])-1)*sd(result[,j])/sqrt(length(result[,j]))
  left = m-error
  right = m+error
  errbar(x=j, y=m, yminus=left, yplus=right, add=TRUE)
  j = j + 1
}
dev.off()

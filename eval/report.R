rm(list=ls())
states = read.csv2("report.csv", dec=".")
states$time = states$time/3600

dev.off()
sparsePlot(x=states$time,y=states$beeInfectionRatio,granularity=1000)
sparsePlot(x=states$time,y=states$numberOfBees,granularity=1000, secondary=TRUE, col="blue4")

sparsePlot = function(x, y, granularity, secondary=FALSE, col="black") {
  if (length(x) != length(y)) {
    warning("x and y do not have the same length")
  }
  step = ceiling(length(x)/granularity)
  if (secondary) {
    par(new=TRUE)
    plot(x[c(seq(1,length(x),step),length(x))], y[c(seq(1,length(y),step), length(y))], type="l", axes=FALSE, ylab="", xlab="", bty="c", col=col)
    axis(4)
    mtext("secondary y-axis label",4) 
  } else {
    plot(x[c(seq(1,length(x),step),length(x))], y[c(seq(1,length(y),step), length(y))], type="l", col=col)
  }
}
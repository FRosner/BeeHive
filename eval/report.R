rm(list=ls())

sparsePlot = function(x, y, granularity=100, secondary=FALSE, col="black", title="Bee Plot") {
  if (length(x) != length(y)) {
    warning("x and y do not have the same length")
  }
  step = ceiling(length(x)/granularity)
  if (secondary) {
    par(new=TRUE)
    plot(x[c(seq(1,length(x),step),length(x))], y[c(seq(1,length(y),step), length(y))], type="l", axes=FALSE, ylab="", xlab="", bty="c", col=col, main=title)
    axis(4)
    mtext("secondary y-axis label",4) 
  } else {
    plot(x[c(seq(1,length(x),step),length(x))], y[c(seq(1,length(y),step), length(y))], type="l", col=col, main=title)
  }
}

states = read.csv2("report.csv", dec=".")
states$time = states$time/3600

granularity=100
pdf("report_beeInfectionRatio.pdf")
sparsePlot(x=states$time,y=states$beeInfectionRatio,granularity=granularity, title="Bee Infection Ratio")
dev.off()
pdf("report_numberOfBees.pdf")
sparsePlot(x=states$time,y=states$numberOfBees,granularity=granularity, title="Number of Bees")
dev.off()
pdf("report_averageFlowerNectarRatio.pdf")
sparsePlot(x=states$time,y=states$averageFlowerNectarRatio,granularity=granularity, title="Nectar available (%)")
dev.off()

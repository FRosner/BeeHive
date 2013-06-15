rm(list=ls())

sparsePlot = function(x, y, granularity=100, secondary=FALSE, col="black", title="Bee Plot", ylim=c(0,1), xlab="x", ylab="y") {
  if (length(x) != length(y)) {
    warning("x and y do not have the same length")
  }
  step = ceiling(length(x)/granularity)
  if (secondary) {
    par(new=TRUE)
    plot(x[c(seq(1,length(x),step),length(x))], y[c(seq(1,length(y),step), length(y))], type="l", axes=FALSE, ylab="", xlab="", bty="c", col=col, main=title, ylim=ylim, xlab=xlab, ylab=ylab)
    axis(4)
    mtext("secondary y-axis label",4) 
  } else {
    plot(x[c(seq(1,length(x),step),length(x))], y[c(seq(1,length(y),step), length(y))], type="l", col=col, main=title, ylim=ylim, xlab=xlab, ylab=ylab)
  }
}

states = read.csv2("report.csv", dec=".")
states$time = states$time/3600

granularity=50
width=13
xlab="Time (h)"
title="Bee Simulation - Report: Bee Infection Ratio"
pdf("report_beeInfectionRatio.pdf", title=title, width=width)
sparsePlot(x=states$time, y=states$beeInfectionRatio, granularity=granularity, title=title, xlab=xlab, ylab="Bee Infection Ratio")
dev.off()
title="Bee Simulation - Report: Number of Bees Alive"
pdf("report_numberOfBees.pdf", title=title, width=width)
sparsePlot(x=states$time, y=states$numberOfBees, granularity=granularity, title=title, ylim=c(0,max(states$numberOfBees)), xlab=xlab, ylab="Number of Bees")
dev.off()
title="Bee Simulation - Report: Available Nectar"
pdf("report_averageFlowerNectarRatio.pdf", title=title, width=width)
sparsePlot(x=states$time, y=states$averageFlowerNectarRatio, granularity=granularity, title=title, xlab=xlab, ylab="Nectar available (%)")
dev.off()
title="Bee Simulation - Report: Collapsed Hives"
pdf("report_collapsedHiveRatio.pdf", title=title, width=width)
sparsePlot(x=states$time, y=states$collapsedHiveRatio, granularity=granularity, title=title, xlab=xlab, ylab="Collapsed Hives (%)")
dev.off()

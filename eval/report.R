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
title="Bee Infection Ratio"
pdf("report_beeInfectionRatio.pdf", title=title)
sparsePlot(x=states$time, y=states$beeInfectionRatio, granularity=granularity, title=title)
dev.off()
title="Number of Bees"
pdf("report_numberOfBees.pdf", title=title)
sparsePlot(x=states$time, y=states$numberOfBees, granularity=granularity, title=title)
dev.off()
title="Nectar available (%)"
pdf("report_averageFlowerNectarRatio.pdf", title=title)
sparsePlot(x=states$time, y=states$averageFlowerNectarRatio, granularity=granularity, title=title)
dev.off()
title="Collapsed Hives (%)"
pdf("report_collapsedHiveRatio.pdf", title=title)
sparsePlot(x=states$time, y=states$collapsedHiveRatio, granularity=granularity, title=title)
dev.off()

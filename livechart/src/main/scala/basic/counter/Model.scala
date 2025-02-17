package basic.counter

import com.raquo.laminar.api.L.{*, given}

final class Model(initialStep: Int):
    val allowedSteps = List(1, 2, 3, 5, 10)

    val stepVar = Var(initialStep)

    val stepSignal: Signal[Int] = stepVar.signal

    val diffBus = new EventBus[Int]

    val countSignal: Signal[Int] = diffBus.events.scanLeft(initial = 0)(_ + _)

    def setStep(step: Int): Unit = {
        stepVar.update(_ => step)
    }

    def increment: Unit =
        diffBus.emit(stepVar.now())

    def decrement: Unit =
        diffBus.emit(-1 * stepVar.now())
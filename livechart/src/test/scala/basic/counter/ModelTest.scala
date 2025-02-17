package basic.counter

import basic.counter.Model

class ModelTest extends munit.FunSuite:
    test("increment with initial step") {
        val observableModel = ObservableModel(initialStep = 3)

        assert(observableModel.countSignal.now() == 0)

        observableModel.increment

        assert(observableModel.countSignal.now() == 3)
    }

    test("decrement with initial step") {
        val observableModel = ObservableModel(initialStep = 5)

        assert(observableModel.countSignal.now() == 0)

        observableModel.decrement

        assert(observableModel.countSignal.now() == -5)
    }

    test("increment and decrement with different steps") {
        val observableModel = ObservableModel(initialStep = 5)

        assert(observableModel.countSignal.now() == 0)

        observableModel.decrement

        assert(observableModel.countSignal.now() == -5)

        observableModel.setStep(2)

        observableModel.decrement

        assert(observableModel.countSignal.now() == -7)

        observableModel.increment
        observableModel.increment

        assert(observableModel.countSignal.now() == -3)
        
        observableModel.setStep(3)

        observableModel.increment

         assert(observableModel.countSignal.now() == 0)
    }
end ModelTest
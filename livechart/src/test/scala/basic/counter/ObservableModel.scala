package basic.counter

import com.raquo.laminar.api.L.{*, given}
import com.raquo.airstream.ownership.ManualOwner

class ObservableModel(model: Model):
    export model.{decrement, increment, setStep}

    // observe(new ManualOwner) is required to make the signal strict, only then can now() be called on it
    // See https://github.com/raquo/Airstream?tab=readme-ov-file
    val countSignal: StrictSignal[Int] = model.countSignal.observe(new ManualOwner)
    
object ObservableModel:
    def apply(initialStep: Int): ObservableModel = new ObservableModel(Model(initialStep))
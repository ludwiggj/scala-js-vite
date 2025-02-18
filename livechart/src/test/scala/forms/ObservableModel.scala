package forms

import com.raquo.laminar.api.L.{*, given}
import com.raquo.airstream.ownership.ManualOwner

class ObservableModel(model: Model):
    export model.{stateVar, submitter, zipWriter}

    // observe(new ManualOwner) is required to make the signal strict, only then can now() be called on it
    // See https://github.com/raquo/Airstream?tab=readme-ov-file
    def errorSignal(error: FormState => Option[String]): StrictSignal[Option[String]] =
        model.errorSignal(error).observe(new ManualOwner)
    
object ObservableModel:
    def apply(): ObservableModel = new ObservableModel(Model())
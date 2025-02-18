package forms

import forms.Model

class ModelTest extends munit.FunSuite:
    val emptyState = FormState()
    val noOp: String => Unit = _ => ()

    test("zip is reflected in form state") {
        val model = ObservableModel()

        assert(model.stateVar.now() == emptyState)

        model.zipWriter.onNext("12345")

        assert(model.stateVar.now() == FormState(zip = "12345"))
    }

    test("invalid zip") {
        val model = ObservableModel()
        
        model.zipWriter.onNext("123456")

        model.submitter(noOp).onNext(model.stateVar.now())

        assert(model.errorSignal(_.zipError).now() == Some("Zip code must consist of 5 digits"))
    }
end ModelTest
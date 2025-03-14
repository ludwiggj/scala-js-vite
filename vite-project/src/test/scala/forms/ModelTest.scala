package forms

import forms.FormState.zipErrorMsg
import forms.FormState.descriptionErrorMsg

class ModelTest extends munit.FunSuite:
    val emptyState = FormState()
    val noOp: String => Unit = _ => ()

    test("zip is reflected in form state") {
        val model = Model()

        assert(model.stateSignal.now() == emptyState)

        model.setZip("12345")

        assert(model.stateSignal.now() == FormState(zip = "12345"))
    }

    test("invalid zip does not generate error if form is not validated") {
        val model = Model()
        
        model.setZip("123456")

        assert(model.errorSignal(_.zipError).now() == None)
    }

    test("validating invalid zip generates error which can then be cleared") {
        val model = Model()
        var validFormStateMsg = ""
        
        model.setZip("123456")
        model.validate(validFormStateMsg = _)

        assert(model.errorSignal(_.zipError).now() == Some(zipErrorMsg))
        assert(validFormStateMsg == "")

        model.setZip("12345")

        assert(model.errorSignal(_.zipError).now() == None)
    }

    test("description is reflected in form state") {
        val model = Model()

        assert(model.stateSignal.now() == emptyState)

        model.setDescription("A description")

        assert(model.stateSignal.now() == FormState(description = "A description"))
    }

    test("invalid description does not generate error if form is not validated") {
        val model = Model()
        
        // Empty description is invalid

        assert(model.errorSignal(_.descriptionError).now() == None)
    }

    test("validating invalid description generates error which can then be cleared") {
        val model = Model()
        var validFormStateMsg = ""
        
        // Empty description is invalid
        model.validate(validFormStateMsg = _)

        assert(model.errorSignal(_.descriptionError).now() == Some(descriptionErrorMsg))
        assert(validFormStateMsg == "")

        model.setDescription("A description")

        assert(model.errorSignal(_.descriptionError).now() == None)
    }

    test("a valid form") {
        val model = Model()
        var validFormStateMsg = ""
        
        model.setZip("12345")
        model.setDescription("A description")

        // Empty description is invalid
        model.validate(validFormStateMsg = _)

        assert(validFormStateMsg == "State: FormState(12345,A description,false) is valid")
        assert(model.errorSignal(_.zipError).now() == None)
        assert(model.errorSignal(_.descriptionError).now() == None)
    }
end ModelTest
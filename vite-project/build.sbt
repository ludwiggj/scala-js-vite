import org.scalajs.linker.interface.ModuleSplitStyle

lazy val viteProject = project.in(file("."))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    scalaVersion := "3.3.3",

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "livechart" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    // See https://www.scala-js.org/doc/project/module.html
    Compile / scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
      .withModuleSplitStyle(
        ModuleSplitStyle.SmallModulesFor(List("livechart"))
      )
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0",

    // Depend on Laminar
    libraryDependencies += "com.raquo" %%% "laminar" % "17.2.0",

    // Circe
    libraryDependencies += "io.circe" %%% "circe-core" % "0.14.10",
    libraryDependencies += "io.circe" %%% "circe-parser"  % "0.14.10",

    // Java time
    libraryDependencies += "io.github.cquiroz" %%% "scala-java-time" % "2.5.0",

    // Testing frameworks
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0" % Test,
    libraryDependencies += "com.raquo" %%% "domtestutils" % "18.0.1" % Test,
    // Scalatest needed for smooth integration with domtestutils
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.19" % Test,
    // Required for SplitVarSpec
    libraryDependencies += "com.raquo" %%% "ew" % "0.2.0" % Test,

    // Compile / mainClass := Some("basic.HelloWorld")
    // Compile / mainClass := Some("basic.Count")
    // Compile / mainClass := Some("basic.counter.Count")
    // Compile / mainClass := Some("basic.time.Tick")
    // Compile / mainClass := Some("forms.ShowForm")
    // Compile / mainClass := Some("forms.uncontrolled.ListeningToUserInput")
    // Compile / mainClass := Some("forms.uncontrolled.FormsWithoutVars")
    // Compile / mainClass := Some("todomvc.ToDoMVC")
    Compile / mainClass := Some("earthquakes.EarthquakeQueries"),

    // The JavaScript environment in which to run and test Scala.js applications.
    // See https://github.com/scala-js/scala-js-env-jsdom-nodejs
    // Must run following locally:
    //   npm install jsdom
    Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),

    // Following is required as otherwise get this error:
    //   [error] org.scalajs.jsenv.UnsupportedInputException: Unsupported input: List(ESModule(...
    Test / scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.NoModule)
      // This is required to override Compile setting - it's the only valid option for NoModule
      // See https://javadoc.io/doc/org.scala-js/scalajs-linker-interface_2.12/latest/org/scalajs/linker/interface/ModuleSplitStyle$$FewestModules$.html
      .withModuleSplitStyle(
        ModuleSplitStyle.FewestModules
      )
    },

    // See https://docs.scala-lang.org/overviews/compiler-options/#use-compiler-options-with-sbt
    scalacOptions ++= Seq(
      "-feature",
      "-language:implicitConversions", // exploit "trailing comma" syntax so you can add an option without editing this line
    )

    // scalajs-bundler is another option....
    //   https://github.com/scalacenter/scalajs-bundler
    //   https://stackoverflow.com/questions/63958419/scalajs-env-jsdom-nodejs-run-fails-with-unsupportedinputexception
    //   https://stackoverflow.com/questions/64587429/facing-referenceerror-while-running-tests-with-scalajs-bundler
    //   https://github.com/lolgab/scalajs-vite-example?tab=readme-ov-file
  )
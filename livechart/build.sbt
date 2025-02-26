import org.scalajs.linker.interface.ModuleSplitStyle

lazy val livechart = project.in(file("."))
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
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("livechart")))
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

    // Testing framework
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0" % Test,

    // Compile / mainClass := Some("basic.HelloWorld")
    // Compile / mainClass := Some("basic.Count")
    // Compile / mainClass := Some("basic.counter.Count")
    // Compile / mainClass := Some("basic.time.Tick")
    // Compile / mainClass := Some("forms.ShowForm")
    // Compile / mainClass := Some("forms.uncontrolled.ListeningToUserInput")
    // Compile / mainClass := Some("forms.uncontrolled.FormsWithoutVars")
    // Compile / mainClass := Some("todomvc.ToDoMVC")
    Compile / mainClass := Some("earthquakes.EarthquakeQueries")
  )
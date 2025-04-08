plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.room) apply false
}

tasks.register("dependencyGraph") {
    val dependencyGraph = getDependencyGraph()
    val mermaid = createMermaidText(dependencyGraph.joinToString("\n"))
    writeGraphToReadme(mermaid)
}

fun getDependencyGraph(): List<String> {
    val graph = mutableListOf<String>()
    rootProject.subprojects.forEach { project ->
        val parentModule = project.path
        val moduleDependency = buildList {
            project.configurations
                .filter {
                    it.name.lowercase().endsWith("implementation") || it.name.lowercase()
                        .endsWith("api")
                }
                .forEach { config ->
                    config.dependencies
                        .withType(ProjectDependency::class.java)
                        .map { project.project(it!!.path) }
                        .filter { it != project }
                        .forEach { add(it) }
                }
        }
        val projectMermaidGraph = moduleDependency.joinToString("\n") {
            val childModule = it.path
            "${parentModule}[$parentModule] --> ${childModule}[$childModule]"
        }

        graph.add(projectMermaidGraph)
    }
    return graph.toList()
}

fun createMermaidText(graph: String) = """graph TB
$graph
  """

fun writeGraphToReadme(graph: String) {
    val readmePath = File("README.md")


    if (!readmePath.exists()) {
        println("not found README.md")
        return
    }


    val content = readmePath.readText()
    val newMermaidText = """
```mermaid %%dependency graph
$graph
```
    """.trimIndent()
    val regex = Regex(
        """```mermaid\s*%%dependency graph\s*.*?```""",
        RegexOption.DOT_MATCHES_ALL
    )

    val newContent = content.replace(regex, newMermaidText)

    readmePath.writeText(newContent)
}

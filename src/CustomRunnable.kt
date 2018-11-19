class CustomRunnable(val fn: () -> Any?): Runnable {
    var response: Any? = null

    override fun run() {
        response = fn()
    }
}

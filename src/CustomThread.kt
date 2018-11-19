class CustomThread(val fn: () -> Any?) {
    private val runnable = CustomRunnable(fn)
    private val thread = Thread(runnable)

    fun start() {
        this.thread.start()
    }

    fun join() {
        this.thread.join()
    }

    fun getResponse(): Any? {
        return this.runnable.response
    }
}
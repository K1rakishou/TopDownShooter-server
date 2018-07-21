package extensions

inline fun <T> MutableList<T>.removeFirst(crossinline predicate: (value: T) -> Boolean) {
	val each = this.iterator()
	while (each.hasNext()) {
		if (predicate(each.next())) {
			each.remove()
			break
		}
	}
}
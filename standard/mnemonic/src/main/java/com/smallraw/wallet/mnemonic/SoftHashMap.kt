package com.smallraw.wallet.mnemonic

import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference

class SoftHashMap<K, V> : HashMap<K, V> {
    /**
     * queue,软引用标记队列
     *
     * ★★★★★★★ 解释 ★★★★★★★
     * 当SoftNode中 Value 被回收时，SoftNode 对象会被放入 queue中，以表示当前SoftNode 中的Value不存在
     * 对我们的使用好处就是，我们读取 queue 队列，取出 SoftNode对象，取出其内部的 Key
     * 以便于 temp 通过 key remove
     */
    private val queue: ReferenceQueue<V> = ReferenceQueue()

    /**
     * 真正的map对象
     * 1、temp 内部 封装的 Node 强引用 K 和 SoftNode
     * 2、SoftNode 内部强引用K，弱引用真正的Value
     */
    private val temp: HashMap<K, SoftNode<K, V>?> = HashMap()

    constructor() : super()
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor(initialCapacity: Int, loadFactor: Float) : super(initialCapacity, loadFactor)

    override operator fun get(key: K): V? {
        clearQueue()
        // 通过 key进行取值，如果为null，返回null，否则返回 SoftNode 软引用的值
        val softNode = temp.get(key)
        return if (softNode == null) null else softNode.get()
    }

    override fun put(key: K, value: V): V? {
        clearQueue()
        // 创建 SoftNode对象
        val softNode = SoftNode(key, value, queue)
        // 返回key之前所对应的SoftNode对象，即oldSoftNode
        val oldSoftNode = temp.put(key, softNode)
        // 如果oldSoftNode为null，就返回null，否则就返回 oldSoftNode所软引用的 Value
        return oldSoftNode?.get()
    }

    override fun containsKey(key: K): Boolean {
        clearQueue()
        return temp.containsKey(key)
    }

    override fun remove(key: K): V? {
        clearQueue()
        val remove: SoftNode<K, V>? = temp.remove(key)
        return if (remove == null) null else remove.get()
    }

    override val size: Int
        get() {
            clearQueue()
            return temp.size
        }

    /**
     * 通过软引用队列内的 SoftNode，获取Key，然后temp 清除此 Key
     * @see ReferenceQueue poll
     */
    private fun clearQueue() {
        var poll: SoftNode<*, *>?
        while ((queue.poll() as SoftNode<*, *>?).also { poll = it } != null) {
            temp.remove(poll?.key)
        }
    }

    /**
     * 对V进行软引用的类
     * @param <K> key，用于当 V 被回收后，temp 可以通过 key 进行移除
     * @param <V> Value，真正的值
     *
     * 传入的queue，用于当Value被回收后，将 SoftNode对象放入 queue中，
     * 以便于表示 某 SoftNode对象中的Value 已经被收回了。
     */
    private inner class SoftNode<K, V>(var key: K, v: V, queue: ReferenceQueue<in V>) :
        SoftReference<V>(v, queue)
}
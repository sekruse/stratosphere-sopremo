package eu.stratosphere.sopremo.pact;

import org.apache.flink.util.Collector;

import eu.stratosphere.sopremo.EvaluationContext;
import eu.stratosphere.sopremo.expressions.EvaluationExpression;
import eu.stratosphere.sopremo.serialization.SopremoRecord;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * The JsonCollector converts {@link IJsonNode}s to {@link SopremoRecord}s and collects this records with a given
 * Collector.
 */
public class JsonCollector<T extends IJsonNode> implements Collector<T> {

	private Collector<SopremoRecord> collector;

	private final EvaluationExpression resultProjection;

	private final SopremoRecord record;

	/**
	 * Initializes a JsonCollector in the given {@link EvaluationContext}.
	 */
	public JsonCollector(final EvaluationContext context) {
		this.resultProjection = context.getResultProjection();
		this.record = new SopremoRecord();
	}

	/*
	 * (non-Javadoc)
	 * @see eu.stratosphere.api.record.functions.Collector#close()
	 */
	@Override
	public void close() {
		this.collector.close();
	}

	/**
	 * Collects the given {@link IJsonNode}
	 * 
	 * @param value
	 *        the node that should be collected
	 */
	@Override
	public void collect(final T value) {
		final IJsonNode resultValue = this.resultProjection.evaluate(value);
		if (SopremoUtil.DEBUG && SopremoUtil.LOG.isTraceEnabled())
			SopremoUtil.LOG.trace(String.format(" to %s", resultValue));
		this.record.setNode(resultValue);
		this.collector.collect(this.record);
	}
	
	public SopremoRecord wrap(final T value) {
		final IJsonNode resultValue = this.resultProjection.evaluate(value);
		if (SopremoUtil.DEBUG && SopremoUtil.LOG.isTraceEnabled())
			SopremoUtil.LOG.trace(String.format(" to %s", resultValue));
		this.record.setNode(resultValue);
		return this.record;
	}

	/**
	 * Sets the collector to the specified value.
	 * 
	 * @param collector
	 *        the collector to set
	 */
	public void configure(final Collector<SopremoRecord> collector) {
		this.collector = collector;
	}
}

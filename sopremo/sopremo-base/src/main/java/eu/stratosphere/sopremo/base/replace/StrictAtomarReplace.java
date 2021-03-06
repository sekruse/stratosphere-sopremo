/***********************************************************************************************************************
 *
 * Copyright (C) 2010-2013 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package eu.stratosphere.sopremo.base.replace;

import eu.stratosphere.sopremo.expressions.EvaluationExpression;
import eu.stratosphere.sopremo.expressions.PathSegmentExpression;
import eu.stratosphere.sopremo.operator.InputCardinality;
import eu.stratosphere.sopremo.operator.Internal;
import eu.stratosphere.sopremo.pact.JsonCollector;
import eu.stratosphere.sopremo.pact.SopremoJoin;
import eu.stratosphere.sopremo.type.IJsonNode;

/**
 * Replaces values in the first source by values in the dictionary given in the second source.
 */
@InputCardinality(min = 2, max = 2)
@Internal
public class StrictAtomarReplace extends AtomarReplaceBase<StrictAtomarReplace> {
	public static class Implementation extends SopremoJoin {
		private PathSegmentExpression replaceExpression;

		private EvaluationExpression dictionaryValueExtraction;

		/*
		 * (non-Javadoc)
		 * @see eu.stratosphere.sopremo.pact.SopremoJoin#match(eu.stratosphere.sopremo.type.IJsonNode,
		 * eu.stratosphere.sopremo.type.IJsonNode, eu.stratosphere.sopremo.pact.JsonCollector)
		 */
		@Override
		protected void join(final IJsonNode value1, final IJsonNode value2, final JsonCollector<IJsonNode> out) {
			out.collect(this.replaceExpression.set(value1, this.dictionaryValueExtraction.evaluate(value2)));
		}
	}
}
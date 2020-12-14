/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.env;

/**
 * Parses a {@code String[]} of command line arguments in order to populate a
 * {@link CommandLineArgs} object.
 *
 * <h3>Working with option arguments</h3>
 * Option arguments must adhere to the exact syntax:
 * <pre class="code">--optName[=optValue]</pre>
 * That is, options must be prefixed with "{@code --}", and may or may not specify a value.
 * If a value is specified, the name and value must be separated <em>without spaces</em>
 * by an equals sign ("=").
 *
 * <h4>Valid examples of option arguments</h4>
 * <pre class="code">
 * --foo
 * --foo=bar
 * --foo="bar then baz"
 * --foo=bar,baz,biz</pre>
 *
 * <h4>Invalid examples of option arguments</h4>
 * <pre class="code">
 * -foo
 * --foo bar
 * --foo = bar
 * --foo=bar --foo=baz --foo=biz</pre>
 *
 * <h3>Working with non-option arguments</h3>
 * Any and all arguments specified at the command line without the "{@code --}" option
 * prefix will be considered as "non-option arguments" and made available through the
 * {@link CommandLineArgs#getNonOptionArgs()} method.
 *
 * @author Chris Beams
 * @since 3.1
 */
class SimpleCommandLineArgsParser {

	/**
	 * Parse the given {@code String} array based on the rules described {@linkplain
	 * SimpleCommandLineArgsParser above}, returning a fully-populated
	 * {@link CommandLineArgs} object.
	 * @param args command line arguments, typically from a {@code main()} method
	 *
	 * 解析传入的String[] args 参数
	 */
	public CommandLineArgs parse(String... args) {
		//创建CommandLineArgs对象
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		for (String arg : args) {
			//进循环，判断是否以--开头
			if (arg.startsWith("--")) {
				//如果是就截取，截取长度是参数长度，如果命令是--spring，那么就从s开始截取，包括s
				String optionText = arg.substring(2, arg.length());
				String optionName;
				String optionValue = null;
				//如果匹配到了=号，截取=号左边做optionName，右边做optionValue
				if (optionText.contains("=")) {
					optionName = optionText.substring(0, optionText.indexOf('='));
					optionValue = optionText.substring(optionText.indexOf('=')+1, optionText.length());
				}
				else {
					//如果没有=号，optionName 直接就是截取的optionText
					optionName = optionText;
				}
				if (optionName.isEmpty() || (optionValue != null && optionValue.isEmpty())) {
					throw new IllegalArgumentException("Invalid argument syntax: " + arg);
				}
				//这里将解析的参数添加到上面创建的CommandLineArgs对象中，该对象中有一个Map<String, List<String>>来存放
				commandLineArgs.addOptionArg(optionName, optionValue);
			}
			else {
				//不是--开头就是直接添加到非选项参数
				commandLineArgs.addNonOptionArg(arg);
			}
		}
		return commandLineArgs;
	}

}

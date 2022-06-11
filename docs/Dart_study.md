[toc]

Dart语言官方网址：https://www.dartcn.com/guides/language/language-tour

Dart语言概览：https://dart.cn/overview



#### 1. 重要概念

在学习 Dart 语言时, 应该基于以下事实和概念：

- 任何保存在变量中的都是一个 对象 ， 并且所有的对象都是对应一个**类**的实例。 无论是数字，函数和 null 都是对象。所有对象继承自 **Object** 类。
- Dart 是强类型的，但是 Dart 可以推断类型，所以类型注释是可选的。可以`dynamic`关键字指明为动态类型。
- Dart 支持泛型，如 `List <int>` （整数列表）或 `List <dynamic>` （任何类型的对象列表）。
- Dart支持顶级函数与变量。
- 与 Java 不同，Dart 没有关键字 `public`， `protected`和 `private`。 如果标识符以下划线`_`开头，则它相对于库是私有的



#### 2. 变量

创建变量的方式有：

- 使用`var`关键字隐式推断类型
- 使用内置类型关键字显示声明并定义
- 使用`dynamic`创建动态类型的变量

```dart
var name1 = "Bob"; // 隐式推断为String
String name2 = "Bob"; // 显示声明并定义
dynamic name3 = "Bob";  // 使用动态类型，后续可变更类型
name3 = 123456;
```

> 未初始化的变量默认值是 `null`。即使变量是数字 类型默认值也是 null，因为在 Dart 中一切都是对象，数字类型 也不例外。



可以使用`final` 或 `const`定义常量，其中`const`是在编译时期就固定变量的值。



Dart 语言支持以下**内建类型**：

- Number：有**int**和double类型
- String：是一组 UTF-16 单元序列，串通过单引号或者双引号创建字符串。
- Boolean：使用 **bool** 类型表示布尔值。  Dart 只有字面量 `true` and `false` 是布尔类型， 这两个对象都是编译时常量。
- List (也被称为 *Array*)：在 Dart 中的 **Array 就是 List 对象**， 通常称之为 List 。使用`[]`创建。
- Map：通常来说， Map 是用来关联 keys 和 values 的对象。使用`{}`创建空的Map，形式如`{key:value}`
- Set：在 Dart 中 Set 是一个元素唯一且无需的集合。可使用`<T>{}`创建空集合，
- Rune (用于在字符串中表示 Unicode 字符)
- Symbol



#### 3. 函数

Dart 是一门真正面向对象的语言， 甚至其中的函数也是对象，并且有它的类型 **Function** 。 

以下函数使用范例：

```dart
bool isNoble(int atomicNumber) {
  return _nobleGases[atomicNumber] != null;
}

// 1. 如果函数中只有一句表达式，可以使用箭头语法
bool isNoble(int atomicNumber) => _nobleGases[atomicNumber] != null;

// 2. 使用 {type field} 命名可选参数，函数调用 enableFlags(bold:true,hidden:false);
void enableFlags({bool bold = true, bool hidden = false}) {
  print("enableFlags bold=$bold, hidden=$hidden");
}

// 3. 使用 [type field] 位置可选参数，如不传入device，say("Bob", "Hello");
// 4. 2和3均使用了默认参数，如：String device = "device"
String say(String from, String msg, [String device = "device"]) {
  var result = '$from says $msg';
  if (device != null) {
    result = '$result with a $device';
  }
  return result;
}

// 5. list 或 map 可以作为默认值传递。
void doStuff(
    {List<int> list = const [1, 2, 3],
    Map<String, String> gifts = const {
      'first': 'paper',
      'second': 'cotton',
      'third': 'leather'
    }}) {
  print('list:  $list');
  print('gifts: $gifts');
}

// 6. 匿名函数，如下例子item是list中的String子项
var list = ['apples', 'bananas', 'oranges'];
list.forEach((item) {
  print('${list.indexOf(item)}: $item');
});

// 7. 语法作用域，此时在nestedFunction()函数可以访问所有的变量，一直到顶级作用域变量。
bool topLevel = true;

void main() {
  var insideMain = true;

  void myFunction() {
    var insideFunction = true;

    void nestedFunction() {
      var insideNestedFunction = true;
    }
  }
}

// 8. 使用函数变量
void foo() {} //声明并定义一个函数
void main(){
    var x = foo; // 持有这个函数地址
    if(x is Function){ // 判断x为Function对象类型
        print("x is Function");
    }
    x(); // 函数调用
}
```



#### 4. 运算符

Dart 支持常用的算术运算符，除了常用的加减乘除外，还有如下区别：

- `/`：整除，结果是浮点数
- `~/`：整除，结果是整型

算术运算符示例如下：

```dart
assert(2 + 3 == 5);
assert(2 - 3 == -1);
assert(2 * 3 == 6);
assert(5 / 2 == 2.5); // 结果是双浮点型
assert(5 ~/ 2 == 2);  // 结果是整型
assert(5 % 2 == 1);   // 余数
```



Dart提供类型判断符，`as`， `is`， 和 `is!` 运算符可用于运行时处理类型检查：

```dart
// 1. 类型检查
if (emp is Person) {
    emp.firstName = 'Bob';
}

// 2. 类型转换
(emp as Person).firstName = 'Bob';
```



下面将列举一些未提及的运算符：

```dart
// 1. 赋值运算符 ??
// 如果b为空时，将变量赋值给b，否则，b的值保持不变。
b ??= value;

// 2. 条件运算符 ? :
var visibility = isPublic ? 'public' : 'private';
// 使用??判断name是否为non-null，
String playerName(String name) => name ?? 'Guest';

// 3. 级联运算符 ..
querySelector('#confirm') // 获取对象。
  ..text = 'Confirm' // 调用成员变量。
  ..classes.add('important')
  ..onClick.listen((e) => window.alert('Confirmed!'));
// 等价于如下代码
var button = querySelector('#confirm');
button.text = 'Confirm';
button.classes.add('important');
button.onClick.listen((e) => window.alert('Confirmed!'));
```

另外，对于Dart的控制流程语句，和Java是基本一致的用法。



#### 5. 异常

Dart 代码可以抛出和捕获异常，它的所有异常是**非检查异常**。 Dart方法不会声明它们抛出的异常， 也不要求捕获任何异常。

如下是常用的一个try-catch用法：

```dart
try {
  breedMoreLlamas();
} on OutOfLlamasException {
  // 一个特殊的异常
  buyMoreLlamas();
} on Exception catch (e) {
  // 其他任何异常
  print('Unknown exception: $e');
} catch (e) {
  // 没有指定的类型，处理所有异常
  print('Something really unknown: $e');
}
// catch() 函数可以指定1到2个参数， 第一个参数为抛出的异常对象， 第二个为堆栈信息
/*catch (e, s) {
  print('Exception details:\n $e');
  print('Stack trace:\n $s');
}*/
```

与Java一样，存在try-catch-finally用法：

```dart
try {
  breedMoreLlamas();
} catch (e) {
  print('Error: $e'); // Handle the exception first.
} finally {
  cleanLlamaStalls(); // Then clean up.
}
```



#### 6. 类

Dart中所有的类都继承于 Object，如下是关于类的使用方式：

```dart
// 1. 可以省略 new 关键字创建对象
var p = Point(2, 2);
// 2. 使用 ?. 进行 non-null判断
p?.y = 3;
// 3. 使用 runtimeType 属性获取运行时对象类型，返回Type对象
print('The type of a is ${a.runtimeType}');
```




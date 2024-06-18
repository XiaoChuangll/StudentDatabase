# SQL 查询任务

## 1. 查询雇员信息
从 `Employees` 表中查询所有雇员的以下信息：雇员编号，姓氏，名字，职务和出生日期。

```sql
SELECT 
    EmployeeID AS '雇员编号',
    LastName AS '姓氏',
    FirstName AS '名字',
    Title AS '职务',
    Birthdate AS '出生日期'
FROM Employees;
2. 查询产品信息
从 Products 表中查询出价格在 20 美元到 50 美元之间的产品的以下信息：产品编号，产品名字，类别编号，供货商编号，单价，库存量。

sql
复制代码
SELECT 
    ProductID AS '产品编号',
    ProductName AS '产品名字',
    CategoryID AS '类别编号',
    SupplierID AS '供货商编号',
    UnitPrice AS '单价',
    UnitsInStock AS '库存量'
FROM Products
WHERE UnitPrice BETWEEN 20 AND 50;
3. 查询特定时间段订单
查询一下 1997 年第二季度的所有订单信息。

sql
复制代码
SELECT *
FROM Orders
WHERE YEAR(OrderDate) = 1997 AND MONTH(OrderDate) IN (4, 5, 6);
4. 查询特定条件的客户信息
从 Customers 表中查询出 ContactName 以“n”结尾的客户公司信息。

sql
复制代码
SELECT *
FROM Customers
WHERE ContactName LIKE '%n';

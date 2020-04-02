package com.example.infosecure.entity;

public class ConPerson {
    private String name;
    private String phone1;
    private String phone2;

    public ConPerson(String name,String phone1,String phone2){
        this.name=name;
        this.phone1 = phone1;
        this.phone2=phone2;
    }
    public boolean equals(Object obj)//重写equals方法,原先的是判断是否同一对象，现在的是判断字符串
    {
        if (!(obj instanceof ConPerson))//instanceof 运算符是用来在运行时指出对象是否是特定类的一个实例
            return false;
        ConPerson p = (ConPerson) obj;
        return this.name.equals(p.name)&& this.phone1.equals(p.phone1);
        //this是调用者，表示后传入对象 调用equals()方法跟参数p.name进行比较
        //name是String对象，调用String的equals()方法判断字符串内容是否相等
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

}

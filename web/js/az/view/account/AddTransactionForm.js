Ext.define('alexzam.his.view.account.AddTransactionForm', {
    extend:'Ext.form.Panel',

    requires:[
        'Ext.form.RadioGroup',
        'Ext.form.field.Date',
        'Ext.form.field.Number',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.form.field.Radio',
        'alexzam.his.model.account.proxy.CategoryAdd',
        'alexzam.his.model.account.store.Category'
    ],

    title:'Добавить транзакцию',
    layout:'hbox',
    bodyPadding:5,
    autoScroll:true,
    items:[
        null,
        {
            xtype:'datefield',
            fieldLabel:'Когда',
            id:'tbAddDate',
            name:'date',
            maxValue:new Date(),
            format:'d.m.Y',
            validateOnChange:false,
            allowBlank:true,
            labelWidth:40
        },
        {
            xtype:'panel',
            layout:'vbox',
            height:80,
            border:0,
            bodyPadding:'0 0 0 5px',
            items:[
                {
                    xtype:'panel',
                    layout:'hbox',
                    width:250,
                    border:0,
                    items:[
                        {
                            xtype:'numberfield',
                            name:'amount',
                            fieldLabel:'Сколько',
                            minValue:0.01,
                            hideTrigger:true,
                            keyNavEnabled:false,
                            mouseWheelEnabled:false,
                            labelWidth:50,
                            validateOnChange:false,
                            width:155,
                            labelAlign:'left',
                            allowBlank:true,
                            id:'tbAddAmount'
                        },
                        {
                            xtype:'panel',
                            border:0,
                            html:'<span class="labCurrency">RUR</span>'
                        }
                    ]
                },
                {
                    xtype:'radiogroup',
                    width:250,
                    layout:'vbox',
                    height:50,
                    allowBlank:false,
                    listeners:{
//                        change:account.onAddTypeChange
                    },
                    items:[
                        {
                            xtype:'panel',
                            layout:'hbox',
                            border:0,
                            width:250,
                            items:[
                                {
                                    xtype:'radio',
                                    boxLabel:'Трата из своих',
                                    name:'type',
                                    inputValue:'p',
                                    margin:'0 5 0 0'
                                },
                                {
                                    xtype:'radio',
                                    boxLabel:'Трата из Казны',
                                    name:'type',
                                    inputValue:'a'
                                }
                            ]
                        },
                        {
                            xtype:'panel',
                            layout:'hbox',
                            border:0,
                            width:250,
                            items:[
                                {
                                    xtype:'radio',
                                    boxLabel:'Вклад в Казну',
                                    name:'type',
                                    inputValue:'i',
                                    margin:'0 5 0 0'
                                },
                                {
                                    xtype:'radio',
                                    boxLabel:'Возмещение из Казны',
                                    name:'type',
                                    inputValue:'r'
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            xtype:'panel',
            layout:'vbox',
            height:80,
            border:0,
            bodyPadding:'0 5px 0 5px',
            items:[
                {
                    xtype:'combo',
                    fieldLabel:'Категория',
                    name:'cat',
                    id:'cbCategory',
                    queryMode:'local',
                    store:'stCats',
                    valueField:'id',
                    displayField:'name',
                    lastQuery:'',
                    labelWidth:80,
                    allowBlank:true
                },
                {
                    xtype:'textfield',
                    fieldLabel:'Комментарий',
                    name:'comment',
                    labelWidth:80
                }
            ]
        },
        {
            xtype:'button',
            text:'Добавить'
//            ,
            //handler:account.addSubmit
        }
    ],

    rgActor:null,

    initComponent:function ()
    {
        var me = this;
        var store = Ext.create('alexzam.his.model.account.store.Category', {
            storeId:'stCats',
            proxy:Ext.create('alexzam.his.model.account.proxy.CategoryAdd', {
                rootUrl:me.rootUrl
            })
        });

        me.rgActor = Ext.create('Ext.form.RadioGroup', {
            fieldLabel:'Кто',
            labelAlign:'top',
            vertical:true,
            columns:1,
            minWidth:100,
            allowBlank:false,
            items:me.userRadioOptions
        });

        me.items[0] = me.rgActor;

        me.callParent();
    }
});
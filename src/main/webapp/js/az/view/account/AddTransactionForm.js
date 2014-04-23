Ext.define('alexzam.his.view.account.AddTransactionForm', {
    extend: 'Ext.form.Panel',

    requires: [
        'Ext.form.RadioGroup',
        'Ext.form.field.Date',
        'Ext.form.field.Number',
        'Ext.form.field.ComboBox',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.form.field.Radio',
        'alexzam.his.model.account.proxy.CategoryAdd',
        'alexzam.his.model.account.store.Category',
        'alexzam.his.view.account.TrTypeRadioGroup'
    ],

    title: 'Новая транзакция',
    layout: {
        type: 'table',
        columns: 4
    },
    bodyPadding: 5,
    autoScroll: true,
    items: [
        {
            xtype: 'button',
            text: 'Расход',
            itemId: 'btnType',
            menu: [
                {
                    xtype: 'menucheckitem',
                    group: 'type',
                    text: 'Расход',
                    checked: true
                },
                {
                    xtype: 'menucheckitem',
                    group: 'type',
                    text: 'Приход'
                },
                {
                    xtype: 'menucheckitem',
                    group: 'type',
                    text: 'Перевод'
                }
            ]
        },
        {
            xtype: 'combo',
            fieldLabel: 'Категория',
            name: 'cat',
            itemId: 'cbCategory',
            queryMode: 'local',
            valueField: 'id',
            displayField: 'name',
            lastQuery: '',
            labelWidth: 80,
            allowBlank: true,
            listeners: {
                blur: function () {
                    var value = this.getValue();
                    this.setValue((value != null) ? value.trim() : null);
                }
            }
        },
        {
            xtype: 'numberfield',
            name: 'amount',
            fieldLabel: 'Сколько',
            minValue: 0.01,
            hideTrigger: true,
            keyNavEnabled: false,
            mouseWheelEnabled: false,
            labelWidth: 50,
            validateOnChange: false,
            labelAlign: 'left',
            allowBlank: true,
            itemId: 'numAmount'
            /*
             * {
             xtype: 'panel',
             border: 0,
             html: '<span class="labCurrency">р.</span>'
             }*/
        },
        {
            xtype: 'button',
            text: 'Добавить',
            itemId: 'btSubmit',
            rowspan: 2,
            handler: function () {
                this.ownerCt.onBtSubmit();
            }
        },
        {
            xtype: 'datefield',
            fieldLabel: 'Когда',
            name: 'date',
            maxValue: new Date(),
            format: 'd.m.Y',
            validateOnChange: false,
            allowBlank: true,
            labelWidth: 50,
            itemId: 'dtAdd',
            startDay: 1
        },
        {
            html: 'общий?'
        },
        {
            xtype: 'textfield',
            fieldLabel: 'Комментарий',
            name: 'comment',
            itemId: 'tbComment',
            labelWidth: 80,
            maxLength: 255,
            maxLengthText: "Комментарий не должен быть длиннее 255 символов"
        }
    ],

    cbCategory: null,
    dtAdd: null,
    numAmount: null,
    tbComment: null,
    storeCat: null,
    btnType: null,

    initComponent: function () {
        var me = this;
        me.storeCat = Ext.create('alexzam.his.model.account.store.Category', {
            proxy: Ext.create('alexzam.his.model.account.proxy.CategoryAdd', {
                rootUrl: me.rootUrl
            }),
            storeId: 'cat-add'
        });

//        me.items[3].items[0].store = me.storeCat;

        me.callParent();

        me.btnType = me.getComponent('btnType');
        Ext.each(me.btnType.menu.items.items, function (item) {
            item.on('checkchange', me.onTypeChanged, me);
        });
//        me.getComponent('rgTrType').on('typechanged', me.onTypeChanged, me);

//        me.cbCategory = me.getComponent('panel3').getComponent('cbCategory');
//        me.dtAdd = me.getComponent('panel1').getComponent('dtAdd');
//        me.numAmount = me.getComponent('panel1').getComponent('panel0').getComponent('numAmount');
//        me.tbComment = me.getComponent('panel3').getComponent('tbComment');

        me.addEvents('transchanged');
    },

    onTypeChanged: function (item, checked) {
        if (!checked)return;
        this.btnType.setText(item.text);
        console.log('changed');
    },

    setCmpValidation: function (cmp, enable) {
        cmp.allowBlank = !enable;
        cmp.isValid();
    },

    setFullValidation: function (enable) {
        var me = this;
        me.setCmpValidation(me.dtAdd, enable);
        me.setCmpValidation(me.numAmount, enable);
        me.setCmpValidation(me.cbCategory, enable);
    },

    onBtSubmit: function () {
        var me = this;

        if (!me.getForm().isValid()) return;

        var data = me.getValues();

        me.setFullValidation(true);

        if (!me.dtAdd.isValid() || !me.numAmount.isValid() || !me.cbCategory.isValid()) {
            return;
        }

        data.date = me.dtAdd.getValue().getTime();
        if (me.cbCategory.getRawValue() == data.cat) {
            // New category
            data.catname = data.cat;
            data.cat = 0;
        }

        if (data.actor == 0) data.actor = me.uid;
        data.act = 'put';

        Ext.Ajax.request({
            url: me.rootUrl + 'account/data',
            params: data,
            success: function () {
                me.fireEvent('transchanged');
                me.reloadCats();
            }
        });

        me.resetForm();
        me.setFullValidation(false);
    },

    resetForm: function () {
        var me = this;
        me.cbCategory.setValue(null);
        me.numAmount.setValue(null);
        me.tbComment.setValue(null);
    },

    reloadCats: function () {
        var me = this;
        var cmp = me.cbCategory;
        var val = cmp.getValue();
        cmp.setValue('');
        me.storeCat.load();
        cmp.setValue(val);
        cmp.getPicker().setLoading(false);
    }
});
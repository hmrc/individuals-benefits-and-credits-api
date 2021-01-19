<table>
    <col width="25%">
    <col width="35%">
    <col width="40%">
    <thead>
    <tr>
        <th>Scenario</th>
        <th>Payload</th>
        <th>Response</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><p>Working Tax Credit data found</p>
        <td>
            <p>matchId=&lt;obtained from Individuals Matching API example: 57072660-1df9-4aeb-b4ea-cd2d7f96e430&gt;</p>
            <p>fromDate=2016-01-01<br>toDate= 2017-03-01</p>
        </td>
        <td><p>200 (OK)</p><p>Payload as response example above</p></td>
    </tr>
    <tr>
        <td>Missing MatchId</td>
        <td>matchId query parameter missing</td>
        <td><p>400 (Bad Request)</p>
        <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;matchId is required&quot; }</p></td>
    </tr>
    <tr>
          <td>Missing fromDate</td>
          <td>fromDate query parameter missing</td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;BAD_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate is required" }</p></td>
    </tr>
    <tr>
          <td>toDate earlier than fromDate</td>
          <td>
            <p>Any valid dates where toDate is earlier than fromDate.</p>
            <p>For example:<br>fromDate=2017-01-01<br>toDate=2016-01-01</p>
          </td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;BAD_REQUEST&quot;,<br/>&quot;message&quot; : &quot;Invalid time period requested" }</p></td>
    </tr>
    <tr>
          <td>startTaxYear earlier than the current tax year minus 6</td>
          <td>For example, startTaxYear=2008-09-01 </td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;BAD_REQUEST&quot;,<br/>&quot;message&quot; : &quot;startTaxYear is earlier than maximum allowed" }</p></td>
    </tr>
    <tr>
          <td>fromDate requested is earlier than available data</td>
          <td>
            <p>fromDate earlier than 6 April 2015.</p> 
            <p>For example, fromDate=2015-02-28</p>
          </td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate earlier than 31 March 2015" }</p></td>
    </tr>
    <tr>
          <td>Invalid date format</td>
          <td>Any date that is not ISO 8601 extended format. For example, 20170101</td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate invalid date format" }</p></td>
    </tr>
    <tr>
          <td>Incorrect matchId</td>
          <td>The matchId is not valid</td>
          <td><p>404 (Not Found)</p>
          <p>{ &quot;code&quot; : &quot;NOT_FOUND&quot;,<br/>&quot;message&quot; : &quot;The resource cannot be found" }</p></td>
    </tr>
  </tbody>
</table>
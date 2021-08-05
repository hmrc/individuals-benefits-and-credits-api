<table>
    <col width="25%">
    <col width="35%">
    <col width="40%">
    <thead>
    <tr>
        <th>Scenario</th>
        <th>Parameters</th>
        <th>Response</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><p>A valid, successful request for Working Tax Credit data</p>
        <td>
            <p>The matchId is obtained from the Individuals Matching API. For example: 57072660-1df9-4aeb-b4ea-cd2d7f96e430</p>
            <p>fromDate=2019-01-01<br>toDate=2020-03-01</p>
        </td>
        <td><p>200 (OK)</p><p>Payload as response example above</p></td>
    </tr>
    <tr>
        <td><p>Missing MatchId</p></td>
        <td><p>The request is missing the matchId. Check the query parameters section for what should be included.</p></td>
        <td><p>400 (Bad Request)</p>
        <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;matchId is required&quot; }</p></td>
    </tr>
    <tr>
          <td><p>Missing fromDate</p></td>
          <td><p>The request is missing a fromDate. Check the query parameters section for what should be included.</p></td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;BAD_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate is required" }</p></td>
    </tr>
    <tr>
          <td><p>toDate earlier than fromDate</p></td>
          <td>
            <p>Any valid dates where the toDate is earlier than the fromDate.</p>
            <p>For example:<br>fromDate=2020-01-01<br>toDate=2019-01-01</p>
          </td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;BAD_REQUEST&quot;,<br/>&quot;message&quot; : &quot;Invalid time period requested" }</p></td>
    </tr>
    <tr>
          <td><p>fromDate earlier than current tax year minus 6</p></td>
          <td>
            <p>The fromDate is earlier than the current tax year minus 6.</p>
            <p>For example:<br>fromDate=2014-01-01</p></td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;BAD_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate is earlier than maximum allowed" }</p></td>
    </tr>
    <tr>
          <td><p>fromDate requested is earlier than available data</p></td>
          <td>
            <p>fromDate earlier than 6 April 2015.</p> 
            <p>For example, fromDate=2015-02-28</p>
          </td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate earlier than 31 March 2015" }</p></td>
    </tr>
    <tr>
          <td><p>Invalid date format</p></td>
          <td><p>Any date that is not ISO 8601 extended format. Check the query parameters section for the correct format.</p></td>
          <td><p>400 (Bad Request)</p>
          <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;fromDate invalid date format" }</p></td>
    </tr>
    <tr>
          <td><p>No data found for the provided matchId</p></td>
          <td><p>The matchId has no related data.</p></td>
          <td><p>404 (Not Found)</p>
          <p>{ &quot;code&quot; : &quot;NOT_FOUND&quot;,<br/>&quot;message&quot; : &quot;The resource cannot be found" }</p></td>
    </tr>
    <tr>
        <td><p>Missing CorrelationId</p></td>
        <td><p>The CorrelationId is missing. Check the request headers section for what should be included.</p></td>
        <td>
            <p>400 (Bad Request)</p>
            <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;CorrelationId is required&quot; }</p>
        </td>
    </tr>
    <tr>
        <td><p>Malformed CorrelationId</p></td>
        <td><p>The CorrelationId is in the incorrect format. Check the request headers section for the correct format.</p></td>
        <td>
            <p>400 (Bad Request)</p>
            <p>{ &quot;code&quot; : &quot;INVALID_REQUEST&quot;,<br/>&quot;message&quot; : &quot;Malformed CorrelationId&quot; }</p></td>
        </td>
    </tr>
  </tbody>
</table>